# EWS (Exchange Web Service) CLI

Simple CLI client for Microsoft Exchange Web Service powered by [ews-java-api](https://github.com/OfficeDev/ews-java-api).

## TL;DR

#### List folders

```
$ ewscli mail describe-folders | jq -r .displayName
Inbox
folderA
folderB
folderC
︙
```

#### List top mails in Inbox

```
$ ewscli mail describe-mails --folder-name Inbox --max 10 | jq -r .subject
Hello1
Test mail
︙
```

#### Send mail

```
$ ewscli mail create-mail --to=user@example.com --subject="Hello" --body="<b>IMPORTANT NOTIFICATIONM</b><br>Hello ..."
```

#### Get all the mails sent from user today

```
$ ewscli mail describe-mails --folder-name="Inbox" --query 'From:user@example.com AND Received:today' | jq -r .subject
Hello from user
︙
```

See how to write [query string](https://docs.microsoft.com/en-us/exchange/client-developer/web-service-reference/querystring-querystringtype).

#### Delete all mails under the folder

```
$ ewscli mail delete-mails --folder-name="Folder1" --mode=soft
$ ewscli mail delete-mails --folder-name="Deleted Items" --mode=soft
```

#### Monitor new incoming mails under Inbox and do something

```bash
#!/bin/sh
while read -r json;
do
  doSomething "$json"
done < <(ewscli mail monitor-folders --folder-name "Inbox")
```

# Install

## macOS

Install:

```
$ brew install greymd/tools/ewscli
```

Uninstall:

```
$ brew uninstall ewscli
$ rm -rf ~/.config/ewscli/
```

## Ubuntu

Install:

```
$ wget https://github.com/greymd/ewscli/releases/download/v1.0.7/ewscli-1.0.7-x86_64-linux.deb
$ sudo dpkg -i ewscli-*
```

Uninstall:

```
$ sudo apt remove ewscli
$ rm -rf ~/.config/ewscli/
```

## RHEL / CentOS

Install:

```
$ sudo yum install https://github.com/greymd/ewscli/releases/download/v1.0.7/ewscli-1.0.7-x86_64-linux.rpm
```

Uninstall:

```
$ sudo yum remove ewscli
$ rm -rf ~/.config/ewscli/
```

## Windows

Install:

Execute this executable file.
https://github.com/greymd/ewscli/releases/download/v1.0.7/ewscli_installer-1.0.7-x86_64-windows.exe

Uninstall:

Remove ewscli application with general way (i.e Uninstall through Control Panel).

# Getting started

First of all, register the endpoint of EWS, username and password (type twice).

```
$ ewscli configure
EWS endpoint (i.e https://example.com/EWS/exchange.asmx): https://exchange.example.com/EWS/exchange.asmx
Username: username
Password:
Password Again:
Credential is stored in macOS Keychain
Configure is stored in /Users/user/.config/ewscli/config
exchange.example.com is not trusted by ewscli on JVM. Trust it ? [y/n]: y
```

Given credential will be stored on the shown file.
Please be careful that the password is also stored without encryption if the platform is Linux or Window (KeyRing will be used in macOS).

After that, the command will work!

```
$ ewscli mail describe-folders | jq -r .displayName
Inbox
folderA
folderB
folderC
︙
```


# For Developer

## How to build jar

```
$ gradle -x test build
$ java -jar build/libs/ewscli.jar --help
Usage: ewscli [-hV] [COMMAND]
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  general
  mail
  configure  Initialize credentials
```

## How to build runime

```
$ ./gradlew runtime
```

Directory under `./build/image` includes all the portable executive files.

## How to edit the project

Clone this repository and just open the project with [IntellJ IDEA](https://www.jetbrains.com/idea/).
