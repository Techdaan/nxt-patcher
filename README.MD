# RS3 NXT Patcher
This is a small Java application that patches the RuneScape NXT client. The tool generates RSA keys, patches them in the
launcher and client and generates the hash and CRC of the client. It also compresses the client with a LZMA compression
and changes the config URI regex and path based on the settings

# Setup & Usage
Simply make sure the dependencies from Gradle are present (Currently only lzma-java). After that go to 
`info.techsdev.nxt.Settings` and edit the settings to your liking. The RSA keys in there must be extracted from the 
launcher and client manually, this can be done using tools such as IDA pro.

Once the settings are configured simply put the `client.exe` (Your non-patched NXT client) and `launcher.exe` (Your
 non-patched NXT launcher, which can be found at `C:\Program Files\Jagex\RuneScape Launcher`) in a folder named 'library', then run `info.techsdev.nxt.Patcher`

Make sure to save the console output for use later on.