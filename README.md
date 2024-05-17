# SuperDialog - 超级 NPC 对话框

感受划时代的 RPG 色彩！

## 预览

![preview](./image/preview.gif)

## 快速开始

### 命令

| 命令                                                                 | 描述             |
|--------------------------------------------------------------------|----------------|
| `/supernpc value <player> <typeEnum: string, int, boolean> <data>` | 设置玩家持久化数据的值    |
| `/supernpc open <player> <scenes>`                                 | 为玩家打开一个指定的场景   |
| `/supernpc change <player> <scenes>`                               | 暂无实际作用(WIP)    |
| `/supernpc reload`                                                 | 重载配置文件和 Lua 缓存 |
| `/supernpc help`                                                   | 获取帮助           |

只需要 NPC 执行 `/supernpc open Mcayear default` 即可向玩家 `Mcayear` 打开 `default.lua` 编写的对话框。

### config.yml

```yml
dialog:
  scrolling: true # 使用滚动对话框
  scrolling_speed: 1 # 滚动速度，正整数，最小为 1，值越大越快
 ```

### scenes/default.lua

每一个 lua 脚本都是一个场景，你可以在 lua 脚本中定义多个对话框。

不包含后缀的文件名就是命令中的 `<scenes>` 值，如 `default.lua` 就是 `default`。

## Lua 进阶

Lua 语言的语法非常简单，你可以参考 [LuaJava API 官方文档](https://gudzpoz.github.io/luajava/api.html#jclass-type)。

### Lua 函数设计

轻便简洁是设计的初衷，每一个场景都必须包含 `function main()` 与 `function close()` 两个函数。

函数 `main()` 是默认执行的，函数 `close()` 是当对话框关闭时执行的。

```lua
openCount = nil;

function main()
    openCount = share:getIntValue("openCount", 0); -- 读取玩家持久化数据的值
    openCount = openCount + 1;
    local playerName = player:getName()  -- 调用Java方法获取玩家名称
    local entityName = entity:getName().."§r"  -- 调用Java方法获取实体名称
    local title = "Welcome " .. playerName .. "!"
    local description = "You are interacting with " .. entityName .. ".\nopen count: ".. openCount ..".\nChoose again:"
    local options = {"Option 1", "Option 2"}
    return {title, description, options, { "close", "close" }}
end

function close()
    -- 此函数没有返回值。
    share:setIntValue("openCount", openCount);
end
```

### Lua 全局变量设计

我们添加了一些全局变量

| 变量名 | 描述     | Java 类型                                                                                                                       |
|-----|--------|-------------------------------------------------------------------------------------------------------------------------------|
| player | 玩家对象   | [cn.nukkit.Player](https://github.com/CloudburstMC/Nukkit/blob/master/src/main/java/cn/nukkit/Player.java)                                                                                                           |
| entity | 实体对象   | [cn.nukkit.entity.Entity](https://github.com/CloudburstMC/Nukkit/blob/master/src/main/java/cn/nukkit/entity/Entity.java)      |
| share | 共享变量对象 | [cn.vusv.plugin.superdialog.config.LuaShareVariable](./src/main/java/cn/vusv/plugin/superdialog/config/LuaShareVariable.java) |
| tools | 工具对象   | [cn.vusv.plugin.superdialog.config.LuaToolsVariable](./src/main/java/cn/vusv/plugin/superdialog/config/LuaToolsVariable.java) |

### share 变量详解

share 是玩家的数据文件，它会以玩家名字被持久化保存到服务器的 `./plugins/SuperDialog/players/` 目录下。`

我们只提供了基础的 int、string、boolean 类型的读写方法。

## 鸣谢

[gh-proxy](https://github.com/hunshcn/gh-proxy)：提供 gif 图片 proxy CDN
