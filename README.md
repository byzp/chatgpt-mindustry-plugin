## 在mindustry聊天栏工作的chatgpt

[~]https://github.com/byzp/chatgpt-mindustry-plugin.git

它是plugin，但你可以将它作为mod使用，仅安装mod的玩家作主机有效。OfficialApi版本使用官方接口，PrivateApi使用第三方接口。可用命令(注意中间有空格)：/c <message>；/c 重置会话

- src/OfficialApi.java和src/PrivateApi.java只需保留一个

- OfficialApi.java : 请编辑src/OfficialApi.java的第24-28行，然后编译。你也可以在[releases](https://github.com/byzp/chatgpt-mindustry-plugin/releases)获取不含apk_key的jar，然后用[mt管理器](https://mt2.cn/)等支持编辑dex或class的编辑器填入api_key


- PrivateApi.java : 它是通过非官方接口实现的，来自[Earth-K-Plugin for Yunzai-Bot](https://gitee.com/SmallK111407/earth-k-plugin)，请修改plugin.json中main字段(OfficialApi->PrivateApi)并编译，或者在[releases](https://github.com/byzp/chatgpt-mindustry-plugin/releases)获取可直接使用的版本

``` bash
./gradlew jar #为桌面编译
#./gradlew deploy #为Android编译
#使用d8转换jar为dex再重新打包可能更容易
```

- 将build/libs/里编译好的文件放入mod文件夹，启用多人游戏，在聊天栏输入/c <message>

