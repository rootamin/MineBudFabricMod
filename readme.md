In order to use this mod, you have to have the latest version of fabric-api.
\
The built mod is inside build/libs/minebud-2.0.0-1.20.1.jar

-stable version: 2.0.0
<hr>

usage:
By typing:
``/generatecode`` in the chat, it would generate a unique code for the player inside ``config/players`` directory. each player has it's own text file with the player name on it. the content of the text file is like: ``playername:code``
\
You can view your current code by typing ``/showcode`` in chat. by utilizing ``/generatecode`` again, the mod will generate you a new unique code and save the code under the same file as created before.
\
The hashtag shown before the code in the chat, is not saved inside the users files.
\
contact me if you have any problems: rootamin@outlook.com

<hr>


![minebuddymod1](https://rootamin.github.io/minebudmod1.png)
![minebuddymod2](https://rootamin.github.io/minebudmod2.png)
![minebuddymod3](https://rootamin.github.io/minebudmod3.png)

<hr>

Changelogs:
```
-2.0.0: failsafe: a codes.txt file will appear inside config directory. every newly generated code, will get checked whether there are same code as it is inside the codes.txt or not. if they are similar codes, it would generate another code and repeat the same process.(this has not been tested properly)
-2.0.0: failsafe: the codes are now failsafe and will get saved inside the players folder within config directory.
-2.0.0: feature: made the users to be able to generate new codes.
```