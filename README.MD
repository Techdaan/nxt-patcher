# RS3 NXT Patcher
This is a small Java application that patches the RuneScape NXT client. The tool generates RSA keys, patches them in the
launcher and client and generates the hash and CRC of the client. It also compresses the client with a LZMA compression
and changes the config URI regex and path based on the settings

# Setup & Usage
Simply make sure the dependencies from Gradle are present (Currently only lzma-java). After that go to 
`info.techsdev.nxt.Settings` and enter your own RSA keys. If you leave fields empty, they will automatically be 
generated and printed to the console. Leave the launcher keys empty for the first run, those are required for the
 hash. 

Once you configured the patcher, put `client.exe` (Your non-patched NXT client) and `launcher.exe` (Your
 non-patched NXT launcher, which can be found at `C:\Program Files\Jagex\RuneScape Launcher`) in a folder named 
 'library', then run `info.techsdev.nxt.Patcher`

Save the console output when the patcher generates keys for you - you will need them later on. Save the launcher RSA
keys to prevent having to release a new launcher every client update.

# Errors?
When encountering an error please let me know by making an issue on Github and I'll take a look as I can

# Contributing
Please do so if you have anything to contribute! I'll check out pull requests as much as I can.