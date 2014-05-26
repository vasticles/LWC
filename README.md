#Live Wallpaper Creator

==========================================================
THIS IS A PRIVATE REPOSITORY. DISTRIBUTION AND DEMONSTRATION TO THIRD-PARTIES IS ONLY WITH PERMISSION OF AUTHOR
==========================================================

LWC 3 (or 3.5?) can be powered by Wicked Template that is written using mainly LibGdx library.

The core code can be found under Wicked Template. Since LibGdx is cross-platform, there are 3 more folders to cater to Android,
Desktop and Browser. The template was tailored to Android framework, so desktop and browser functionality is as is (for now at least).

The structure of the template is as follows:

 - Main Engine
   - World/WorldRenderer
     - Layers
       - Groups
         - SpriteManager
           - Sprites
             - StateManager
               - States
                 - Effects

Effects are created using the universal java tween engine to interpolate sprite properties.
Supported effects:
- Opacity
- Rotation
- Scale
- Position

At this time JSON deserialization is set up at group level from within layer class using GSON library.
JSON config files can be found under Wicked Template-Android/assets/data folder.
ForegroundEnergy.json can be seen as an example of a properly formatted and legible config file.

Testing is performed on one group per layer for now.

####To Do:
- [ ] Add concurrency
- [ ] Implement AssetManager
- [ ] Expand JSON deserialization to global (Main Engine) level
- [ ] Add angle effect
- [ ] Add an interactive interface
- [ ] Add support for ranges
- [ ] Add support for multiple values for effect creation

####Bugs:
- [ ] Rotation effect gets reset to starting value upon completition
- [ ] Initial spawn is staggered. AssetManager should fix this

####Further Features:
- [ ] Add gif support (consider writing a gif decoder)
- [ ] Implement a variety of custom and built-in tweening functions
- [ ] Experiment with Particle Emitter

