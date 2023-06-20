## 在mindustry聊天栏工作的chatgpt

https://github.com/byzp/chatgpt-mindustry-plugin.git

它是plugin，但你可以把它作为mod使用，所有对话都是单轮

- src/OfficialApi.java和src/PrivateApi.java只需保留一个，若选择后者，请修改plugin.json的main字段

- OfficialApi.java : 将api key填入src/OfficialApi.java并编译


- PrivateApi.java : 它是通过非官方接口实现的，来自[Earth-K-Plugin for Yunzai-Bot](https://gitee.com/SmallK111407/earth-k-plugin)，修改plugin.json的main字段并编译

``` bash
./gradlew jar #为桌面编译
./gradlew deploy #为Android编译
```

- 将build/libs/里编译好的文件放入mod文件夹，启用多人游戏，在聊天栏输入/c message

