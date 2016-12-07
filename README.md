# SSRules

一个简单粗暴的 Shadowsocks 规则编辑器。需要 Root

<a href='https://play.google.com/store/apps/details?id=com.twiceyuan.ssrules&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="200px" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

## 简介

主要用于编辑 Shadowsocks 数据目录（/data/data/com.github.shadowsocks/）下的 acl 文件。shadowsocks 通过这些文件来判断 URL 是否走代理。如果你有需要代理的网站，在 gfwlist 中没有收录，可以编辑 gfwlist 并添加到白名单即可。

支持手动输入域名或者从浏览器分享添加。

建议在使用前先搞清楚 acl 的文件结构，本 app 没有做太多容错处理。

## FAQ

__Q：__这个 app 跟直接编辑 acl 有什么区别？

__A：__没有区别


__Q：__那为什么要装它？

__A：__首先 shadowsocks for Android 有个特性，每次开启代理时会覆盖 data 目录下的 acl 文件为默认的内容，所以如果用文本管理器修改了，也会被 shadowsocks 给恢复（除非你不一直关闭 shadowsocks）。这个 app 做法也一样简单粗暴，自己修改后把写权限给删掉。

   其次如果你要添加 baidu.com 走代理，需要输入的字符串为一些转码后的表达式，手动输入体验并不友好。

   最后，即使你手动修改了，也是需要重启 ss 才会生效，然而，如果不小心多重启了一下，可能文件就被覆盖了。

__Q：__所以这个 app 做的工作就是……

__A：__一个挫比的 GUI，一个猥琐的写保护，一个暴力的自动重启~~，一点微小的工作~~。

__Q：__适配过吗？

__A：__没，我自己设备是 nexus6p，在同事的魅族和 moto 手机上试着跑了一下。ss 是最新版的，因为是 acl 文件名匹配，所以不是最新版的 ss 也可能有问题，之后有可能会做一下 ss 的版本适配。

## License

GPLv2
