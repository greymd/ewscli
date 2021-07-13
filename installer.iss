#include "installer_environment.iss"
#define MyAppName "ewscli"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "Yasuhiro Yamada"
#define MyAppURL "https://gr3.ie"
#define MyAppExeName "ewscli.cmd"

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{F4AE2A37-11D8-436E-971A-7C84D80A344A}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=yes
; Uncomment the following line to run in non administrative install mode (install for current user only.)
;PrivilegesRequired=lowest
OutputBaseFilename=ewscli_installer
Compression=lzma
SolidCompression=yes
WizardStyle=modern
ChangesEnvironment=true

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "package\windows\*"; DestDir: "{userappdata}\{#MyAppName}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{userappdata}\{#MyAppName}\{#MyAppExeName}"

[Tasks]
Name: envPath; Description: "Add to PATH variable"

[Code]
procedure CurStepChanged(CurStep: TSetupStep);
begin
    if (CurStep = ssPostInstall) and IsTaskSelected('envPath')
    then EnvAddPath(ExpandConstant('{userappdata}') + '\' + '{#MyAppName}' +'\exe');
end;
procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
begin
    if CurUninstallStep = usPostUninstall
    then EnvRemovePath(ExpandConstant('{userappdata}') + '\' + '{#MyAppName}' +'\exe');
end;
