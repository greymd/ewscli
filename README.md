# EWS (Exchange Web Service) CLI

Simple CLI client for Microsoft Exchange Web Service powered by [ews-java-api](https://github.com/OfficeDev/ews-java-api).

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
$ wget https://github.com/greymd/ewscli/releases/download/v1.0.5/ewscli-1.0.5-x86_64-linux.deb
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
$ sudo yum install https://github.com/greymd/ewscli/releases/download/v1.0.5/ewscli-1.0.5-x86_64-linux.rpm
```

Uninstall:

```
$ sudo yum remove ewscli
$ rm -rf ~/.config/ewscli/
```

## Windows

Install:

Execute this executable file.
https://github.com/greymd/ewscli/releases/download/v1.0.5/ewscli_installer-1.0.5-x86_64-windows.exe

Uninstall:

Remove ewscli application with general way (i.e Uninstall through Control Panel).

# Getting started

```
## Set endopoint, username and password
$ ewscli configure
EWS endpoint (i.e https://example.com/EWS/exchange.asmx): https://exchange.example.com/EWS/exchange.asmx
Username: username
Password:
Password Again:
Credential is stored in macOS Keychain
Configure is stored in /Users/user/.config/ewscli/config
exchange.example.com is not trusted by ewscli on JVM. Trust it ? [y/n]: y

## List folders
$ ewscli mail describe-folders | jq -r .displayName
Inbox
folderA
folderB
folderC
...

## List top mails in Inbox
$ ewscli mail describe-mails --folder-name Inbox --max 10 | jq .subject
"Hello1"
"Test mail"
...
```
