# ChatImage

A Minecraft mod which could show image in chat line
在游戏聊天栏中显示你的图片
(使用本地图片 目前只有自己能看到)
### 规范

本项目使用 `ChatImageCode` (`[CICode,<arg>=<value>]`)


| 参数 | 必须 | 类型    | 备注                             |
| ---- | ---- | ------- | -------------------------------- |
| url  | 是   | String  | 图片地址(本地文件请使用file:///) |
| nsfw | 否   | boolean | 暂时没有用                       |
| name | 否   | String  | 在消息栏显示的名称               |

例如：`[CICode,url=https://blog.kituin.fun/img/bg.png,name=Image]`
上述意思为:
网络地址 `url`为 `https://blog.kituin.fun/img/bg.png`
显示名字为 `Image`
`[CICode,url=https://blog.kituin.fun/img/bg.png]` 为合法`CICode`

## 支持方法
- [x] 输入栏直接粘贴图片自动转换`CICode`
- [x] 手动输入`CICode`
- [ ] 使用命令发送`CICode`
## 用法


聊天框输入 `[CICode,url=https://blog.kituin.fun/img/bg.png,name=Image]`  即可自动转换为图片  
本地图片目前只有自己能看到  

![_$SZCFVMV(8}XCEZQG</code></code>Q0L](https://user-images.githubusercontent.com/68675068/214302789-6908add5-79b5-4d0e-b5f0-1ef54cf9fe13.png)
