# EWS (Exchange Web Service) CLI

Simple CLI client for Microsoft Exchange Web Service.
This project is a working in progress project.

# Install

```
$ brew install greymd/tools/ewscli
```

# Getting started

```
## Import TLS certificate first
$ sudo ewscli-import-cert exchange.example.com

## Set endopoint, username and password
$ ewscli configure
EWS endpoint (i.e https://example.com/EWS/exchange.asmx): https://exchange.example.com/EWS/exchange.asmx
Username: username
Password:
Password Again:
Credential is stored in macOS Keychain
Configure is stored in /Users/user/.config/ewscli/config
```
