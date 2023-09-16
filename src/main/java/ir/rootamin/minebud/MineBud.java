package ir.rootamin.minebud;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class MineBud implements ModInitializer {
	public static final String MOD_ID = "minebud";
	private Set<String> usedCodes = new HashSet<>();

	@Override
	public void onInitialize() {
		// Get the config directory
		Path configDir = FabricLoader.getInstance().getConfigDir();

		// Load used codes from file
		File codesFile = new File(configDir.toFile(), "codes.txt");
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

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;

			// Check if the player has joined before
			File playerFile = new File(configDir.toFile(), "players/" + player.getName().getString() + ".txt");
			if (!playerFile.exists()) {
				// Generate a unique code for the player
				String uniqueCode = generateUniqueCode();

				// Save the code to a file
				try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(playerFile)))) {
					out.println(player.getName().getString() + ":" + uniqueCode);
				} catch (IOException e) {
					System.out.println("An error occurred while saving the player's code.");
					e.printStackTrace();
				}
			}
		});

		ServerStartCallback.EVENT.register(server -> {
			server.getCommandManager().getDispatcher().register(CommandManager.literal("showcode").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();

				// Read the player's code from the file
				// Check if the player has joined before
				File playerDir = new File(configDir.toFile(), "players");
				if (!playerDir.exists()) {
					playerDir.mkdirs();
				}
				File playerFile = new File(configDir.toFile(), "players/" + player.getName().getString());
				try (BufferedReader reader = new BufferedReader(new FileReader(playerFile))) {
					String line = reader.readLine();
					String[] parts = line.split(":");
					String code = parts[1];

					// Send the code to the player
					player.sendMessage(Text.of("Your code is: #" + code), false);
				} catch (IOException e) {
					System.out.println("An error occurred while reading the player's code.");
					e.printStackTrace();
				}

				return 1;
			}));

			server.getCommandManager().getDispatcher().register(CommandManager.literal("generatecode").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();

				// Generate a new unique code for the player
				String uniqueCode = generateUniqueCode();

				// Save the new code to the player's file
				// Check if the player has joined before
				File playerDir = new File(configDir.toFile(), "players");
				if (!playerDir.exists()) {
					playerDir.mkdirs();
				}
				File playerFile = new File(configDir.toFile(), "players/" + player.getName().getString());
				try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(playerFile)))) {
					out.println(player.getName().getString() + ":" + uniqueCode);
				} catch (IOException e) {
					System.out.println("An error occurred while saving the player's code.");
					e.printStackTrace();
				}

				// Send the new code to the player
				player.sendMessage(Text.of("Your new code is: #" + uniqueCode), false);

				return 1;
			}));
		});
	}

	private String generateUniqueCode() {
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
		File codesFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "codes.txt");
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(codesFile, true)))) {
			out.println(uniqueCode);
		} catch (IOException e) {
			System.out.println("An error occurred while saving the used codes.");
			e.printStackTrace();
		}

		return uniqueCode;
	}
}