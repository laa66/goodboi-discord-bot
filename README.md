# Goodboi Discord Bot 🤖

Goodboi is a Discord4J powered bot that lets you play and listen to music with your friends on Discord servers. 
The Bot also has queue functionality, so everyone on the same channel will have their time to play their favourite music. 

<hr>

## How to run ⚡

#### To run this application on your local machine. You will need to do the following:

      # Clone this repository to your local machine
      $ git clone https://github.com/laa66/goodboi-discord-bot.git

1. Create ``.env`` file in root project directory and set specific key-value pairs within it:
2. ``GOODBOI_DISCORD_TOKEN``={Discord token from [Discord Developer](https://discord.com/build/app-developers) panel}
3. Generate invite URL in your Discord developer panel and add bot to your server
<!-- end -->

      # Run goodboi-discord-bot using docker-compose in root directory
      $ docker-compose up

#### Congrats! Now you can listen to your favourite music with your friends!

## How to use 🗺️

Here you can check Goodboi music commands:

* ``!goodboi-command`` - list available bot commands

* ``!join`` - the bot joins a voice channel

* ``!play`` <URL> - load and start playing some music from provided Soundcloud URL

* ``!queue`` - list all queued tracks 

* ``!skip`` - play next track from queue 

* ``!stop`` - pause music 

* ``!res`` - resume music 

* ``!clean`` - clear queue and stop playing music

* ``!exit`` - disconnect from voice channel

And here you can check Goodboi guild commands:

* ``/warns`` - list all warned users

* ``/bans`` - list all banned users

* ``/rude`` - list the rudest users

* ``/activity`` - list amount of time users spend on voice channels today

## Features 📌
#### Here, you can check bot features:

* Playing music from ``SoundCloud`` on a voice channel

* Playlist ``queue`` where songs can be added

* Changing the ``player's state`` - skipping to the next song, stopping, resuming and clearing the queue

* Displaying the ``current`` playlist queue

* Tracking ``ban`` and ``unban`` events and adding or removing users from the banned table in the DB accordingly

* Filtering ``messages`` for offensive language

* Assigning ``warnings`` and ``banning`` users who misuse offensive language

* Listing ``banned`` users, ``warned`` users, and those who swear the most

* Daily ``tracking`` of the time users spend on voice channels



## Built with 🔨

#### Technologies & tools used:

- Java 20
- Spring Boot 3
- Reactor
- Discord4J
- LavaPlayer
- PostgreSQL
- Maven
- Project Lombok
- Docker
- IntelliJ IDEA Community Edition

#### Tested with:

- JUnit 5 & AssertJ
- Mockito
- Hamcrest
- Reactor test
- Testcontainers

## To-do 💡

- Analyze active channel volume
- AI-generated voice for bot used for reading private messages from users