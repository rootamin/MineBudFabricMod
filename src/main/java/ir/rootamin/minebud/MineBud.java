package ir.rootamin.minebud;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.*;

public class MineBud implements ModInitializer {
	public static final String MOD_ID = "minebud";

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;

			// Check if the player has joined before
			File playerFile = new File("/home/amin/IdeaProjects/MineBud/run/players/" + player.getName().getString() + ".txt");
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
				File playerFile = new File("/home/amin/IdeaProjects/MineBud/run/players/" + player.getName().getString() + ".txt");
				try (BufferedReader reader = new BufferedReader(new FileReader(playerFile))) {
					String line = reader.readLine();
					String[] parts = line.split(":");
					String code = parts[1];

					// Send the code to the player
					player.sendMessage(Text.of("Your code is: " + code), false);
				} catch (IOException e) {
					System.out.println("An error occurred while reading the player's code.");
					e.printStackTrace();
				}

				return 1;
			}));

			server.getCommandManager().getDispatcher().register(CommandManager.literal("resetcode").executes(context -> {
				ServerPlayerEntity player = context.getSource().getPlayer();

				// Generate a new unique code for the player
				String uniqueCode = generateUniqueCode();

				// Save the new code to the player's file
				File playerFile = new File("/home/amin/IdeaProjects/MineBud/run/players/" + player.getName().getString() + ".txt");
				try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(playerFile)))) {
					out.println(player.getName().getString() + ":" + uniqueCode);
				} catch (IOException e) {
					System.out.println("An error occurred while saving the player's code.");
					e.printStackTrace();
				}

				// Send the new code to the player
				player.sendMessage(Text.of("Your new code is: " + uniqueCode), false);

				return 1;
			}));
		});
	}

	private String generateUniqueCode() {
		// Generate a random alphanumeric string between 6 and 10 characters long
		int length = (int)(Math.random() * 5) + 6;
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder code = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			code.append(allowedChars.charAt((int)(Math.random() * allowedChars.length())));
		}
		return code.toString();
	}
}