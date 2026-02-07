The content blocker is an imroved and extended version of the blocker found in [Yuzu browser](https://github.com/hazuki0x0/YuzuBrowser). It is compatible to hosts lists and AdBlockPlus syntax and the extensions used by AdGuard and uBlock Origin.

## Conflicts between ABP, AdGuard and uBO 

In some cases the same filter is interpreted differently by ABP, AdGuard and uBO
* [host names](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#hosts-files): are interpreted as `||host.tld^` like by uBO
* [document](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#document-for-entire-page-exception): behaves like in uBO
* [strict blocking](https://github.com/gorhill/uBlock/wiki/Strict-blocking): behaves like in uBO
* contrary to uBO, filters like in [this issue](https://github.com/gorhill/uBlock/issues/1065#issue-121994578) work for the listed cases
* [important](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#important-modifier): works on exception rules _and_ blocking rules, as it does in AdGuard. In [uBlock origin](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#important), important works only when used as a blocking rule.

## Incomplete implementation

Some rules are implemented only partially, or not at all.
The most obvious part here is the lack of support for cosmetic filtering (element hiding). An incomplete and not working implementation exists as part left from the original Yuzu blocker. Since it has no impact on privacy, not much effort has been made in making cosmetic filtering work. Contributions are welcome.

**Not fully implemented**
* [modifier filters](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#modifier-filters) may break some websites, even if requests are not modified at all. This is due to some problems when using okhttp to do requests for WebView.
* [badfilter](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#badfilter-modifier) does not work if $domains of badfilter and original filter is different, does not work on `*` and [similar filters with domains](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#badfilter)
* [all](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#all) might not be implemented correctly

**Unsupported, planned to implement**
* [cookie](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#cookie)
* [rewrite](https://blog.adblockplus.org/development-builds/rewriting-url-instead-of-blocking)
* [webrtc](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#webrtc-modifier)
* [header](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#header): still experimental in uBO
* [ping](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#ping): not sure if this is applicable to WebView
* [network](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#network): not sure if this is applicable to WebView
* [cname](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#cname): not sure if this is applicable to WebView
* [pre-parsing directives](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#pre-parsing-directives)

**Unsupported, low priority**
* [element hiding / cosmetic filtering](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#cosmetic-rules)
* [html and js rules](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#app)
* [replace](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#replace-modifier)

**Unsupported filters, no plan to implement**
* [genericblock](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#genericblock)
* [content](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#content-modifier)
* [jsinject](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#jsinject)
* [urlblock](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#urlblock)
* [extension](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#extension)
* [stealth](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#stealth)
* [app](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#app)
* [popup](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#popup-modifier) / [popunder](https://github.com/gorhill/uBlock/wiki/Static-filter-syntax#popunder): not sure if this is applicable to WebView
* [object, object-subrequest](https://kb.adguard.com/en/general/how-to-create-your-own-ad-filters#object-modifier): no need, WebView doesn't have support for such plugins

### failed tests according to [ABP test pages](https://testpages.adblockplus.org)

* Subdocument: **should be supported, but fails**
* XMLHTTPrequest: **should be supported, but fails**
* Third Party: **should be supported, but one of two tests fails**
* CSP: **should be supported, but scrips-src test fails**
* Header: not supported, planned
* WebRTC: not supported, planned
* Rewrite: not supported, planned
* Element hiding: not supported, may be implemented at some point
* Ping: not supported, no indication on failure or success
* WebSocket: not supported, no indication on failure or success
* Popup: all examples open in the same window, thus they are not popups