package ir.rootamin.minebud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MineBud implements ModInitializer {
	public static final String MOD_ID = "minebud";
	private Map<String, String> playerCodes = new HashMap<>();
	private Set<String> usedCodes = new HashSet<>();

	@Override
	public void onInitialize() {
		// Get the config directory
		Path configDir = FabricLoader.getInstance().getConfigDir();

		// Load player codes from JSON file
		File playerCodesFile = new File(configDir.toFile(), "playerCodes.json");
		if (playerCodesFile.exists()) {
			try (Reader reader = new FileReader(playerCodesFile)) {
				Type type = new TypeToken<Map<String, String>>(){}.getType();
				playerCodes = new Gson().fromJson(reader, type);
			} catch (IOException e) {
				System.out.println("An error occurred while loading the player codes.");
				e.printStackTrace();
			}
		}

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;

			// Check if the player has joined before
			if (!playerCodes.containsKey(player.getName().getString())) {
				// Generate a unique code for the player
				String uniqueCode = generateUniqueCode();

				// Save the code to the map
				playerCodes.put(player.getName().getString(), uniqueCode);

				// Save the map to the JSON file
				try (Writer writer = new FileWriter(playerCodesFile)) {
					new Gson().toJson(playerCodes, writer);
				} catch (IOException e) {
					System.out.println("An error occurred while saving the player codes.");
					e.printStackTrace();
				}
			}
		});

		ServerStartCallback.EVENT.register(server -> {
			server.getCommandManager().getDispatcher().register(CommandManager.literal("showcode").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();

				// Get the player's code from the map
				String code = playerCodes.get(player.getName().getString());

				// Send the code to the player
				player.sendMessage(Text.of("Your code is: #" + code), false);

				return 1;
			}));

			server.getCommandManager().getDispatcher().register(CommandManager.literal("generatecode").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();

				// Generate a new unique code for the player
				String uniqueCode = generateUniqueCode();

				// Save the new code to the map and JSON file
				playerCodes.put(player.getName().getString(), uniqueCode);
				try (Writer writer = new FileWriter(playerCodesFile)) {
					new Gson().toJson(playerCodes, writer);
				} catch (IOException e) {
					System.out.println("An error occurred while saving the player codes.");
					e.printStackTrace();
				}

				// Send the new code to the player
				player.sendMessage(Text.of("Your new code is: #" + uniqueCode), false);

				return 1;
			}));
		});
	}

	private String generateUniqueCode() {
		// Load used codes from file
		File codesFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "codes.txt");
		if (codesFile.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(codesFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					usedCodes.add(line);
				}
			} catch (IOException e) {
				System.out.println("An error occurred while loading the used codes.");
				e.printStackTrace();
			}
		}

		String uniqueCode;
		do {
			// Generate a random alphanumeric string between 6 and 10 characters long
			int length = (int)(Math.random() * 5) + 6;
			String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			StringBuilder code = new StringBuilder(length);
			for (int i = 0; i < length; i++) {
				code.append(allowedChars.charAt((int)(Math.random() * allowedChars.length())));
			}
			uniqueCode = code.toString();
		} while (usedCodes.contains(uniqueCode));

		usedCodes.add(uniqueCode);

		// Save the newly generated code to codes.txt
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(codesFile, true)))) {
			out.println(uniqueCode);
		} catch (IOException e) {
			System.out.println("An error occurred while saving the used codes.");
			e.printStackTrace();
		}

		return uniqueCode;
	}
}
