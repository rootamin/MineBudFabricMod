In order to use this mod, you have to have the latest version of fabric-api.
\
The built mod is inside build/libs/minebud-2.1.0-1.20.1.jar

-stable version: 2.1.0-1.20.1
<hr>

usage:
on player login, a code will automatically get generated and by typing ``/showcode``, you can view your own code. by typing ``/generatecode`` in the chat, it would regenerate a unique code for the player. the codes are saved inside ``config/playersCodes.json`` file.
\
The hashtag shown before the code in the chat, is not saved inside the json file.
\
the ``config/codes.txt`` file is all the used codes before. the mod will check if there are any similar codes inside the file before assigning a code for a player. if it finds a similar code, it would regenerate another code and the same process happens until it generates a unique code.
\
contact me if you have any problems: rootamin@outlook.com

<hr>


![minebuddymod4](https://rootamin.github.io/minebudmod4.png)
![minebuddymod5](https://rootamin.github.io/minebudmod5.png)
![minebuddymod6](https://rootamin.github.io/minebudmod6.png)

<hr>

Changelogs:
```
-2.1.0: debug: because of the rework, there is no need for the first player to generate code for mod initialization. the code will get generated when a new player joins the server.
-2.1.0: rework: the codes are now saved inside config/playerCodes.json and no longer each players have their unique text files.
-2.0.0: failsafe: a codes.txt file will appear inside config directory. every newly generated code, will get checked whether there are same code as it is inside the codes.txt or not. if they are similar codes, it would generate another code and repeat the same process.(this has not been tested properly)
-2.0.0: failsafe: the codes are now failsafe and will get saved inside the players folder within config directory.
-2.0.0: feature: made the users to be able to generate new codes.
```