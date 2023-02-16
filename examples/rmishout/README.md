# RMI Shout

## Run exmaple

3 separate terminal windows:
1. `make rmishout`

2. Run the Server code ports between 50010 - 50019 \
`rmiregistry XXXXX`

3. Run the Server \
`java examples.rmishout.ShoutServerMainline XXXXXX YYYYYY`

3. Run the Client code \
`java examples.rmishout.ShoutClient <yourmachine-name> XXXXXX` \
You can use the command `hostname` to identify your host name.