# EWS (Exchange Web Service) CLI

Simple CLI client for Microsoft Exchange Web Service.
This project is a working in progress project.

# Install

## macOS

```
$ brew install greymd/tools/ewscli
```

## Ubuntu

```
$ wget https://github.com/greymd/ewscli/releases/download/v1.0.0/ewscli-1.0.0-x86_64-linux.deb
$ sudo dpkg -i ewscli-*
```

## RHEL / CentOS

```
$ sudo yum install https://github.com/greymd/ewscli/releases/download/v1.0.0/ewscli-1.0.0-x86_64-linux.rpm
```

## Windows

Install this executable file.
https://github.com/greymd/ewscli/releases/download/v1.0.0/ewscli_installer-1.0.0-x86_64-windows.exe

# Getting started

```
## Set endopoint, username and password
$ sudo ewscli configure
EWS endpoint (i.e https://example.com/EWS/exchange.asmx): https://exchange.example.com/EWS/exchange.asmx
Username: username
Password:
Password Again:
Credential is stored in macOS Keychain
Configure is stored in /Users/user/.config/ewscli/config

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
