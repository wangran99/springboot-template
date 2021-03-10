let api = [];
api.push({
    alias: 'AuthorizationController',
    order: '1',
    link: '&lt;p&gt;_认证鉴权请求控制器&lt;/p&gt;',
    desc: '&lt;p&gt; 认证鉴权请求控制器&lt;/p&gt;',
    list: []
})
api[0].list.push({
    order: '1',
    desc: '手机We码认证',
});
api[0].list.push({
    order: '2',
    desc: 'H5轻应用鉴权登录 开放平台文档：https://open.welink.huaweicloud.com/docs/#/qdmtm8/tj778t/wk8q1m?type=third',
});
api[0].list.push({
    order: '3',
    desc: '后台web管理页面免登',
});
api[0].list.push({
    order: '4',
    desc: '用户退出(删除token)',
});
api.push({
    alias: 'LoginUserController',
    order: '2',
    link: '&lt;p&gt;_前端控制器&lt;/p&gt;',
    desc: '&lt;p&gt; 前端控制器&lt;/p&gt;',
    list: []
})
api.push({
    alias: 'TestController',
    order: '3',
    link: '测试服务是否正常启动',
    desc: '测试服务是否正常启动',
    list: []
})
api[2].list.push({
    order: '1',
    desc: '测试返回字符串',
});
api[2].list.push({
    order: '2',
    desc: '测试后台welink调用是否成功',
});
api[2].list.push({
    order: '3',
    desc: '测试数据库读取是否正常',
});
api[2].list.push({
    order: '4',
    desc: '',
});
api[2].list.push({
    order: '5',
    desc: '',
});
api[2].list.push({
    order: '6',
    desc: '',
});
api[2].list.push({
    order: '7',
    desc: '',
});
api.push({
    alias: 'dict',
    order: '4',
    link: 'dict_list',
    desc: '数据字典',
    list: []
})
document.onkeydown = keyDownSearch;
function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code == 13) {
        const search = document.getElementById('search');
        const searchValue = search.value;
        let searchArr = [];
        for (let i = 0; i < api.length; i++) {
            let apiData = api[i];
            const desc = apiData.desc;
            if (desc.indexOf(searchValue) > -1) {
                searchArr.push({
                    order: apiData.order,
                    desc: apiData.desc,
                    link: apiData.link,
                    list: apiData.list
                });
            } else {
                let methodList = apiData.list || [];
                let methodListTemp = [];
                for (let j = 0; j < methodList.length; j++) {
                    const methodData = methodList[j];
                    const methodDesc = methodData.desc;
                    if (methodDesc.indexOf(searchValue) > -1) {
                        methodListTemp.push(methodData);
                        break;
                    }
                }
                if (methodListTemp.length > 0) {
                    const data = {
                        order: apiData.order,
                        desc: apiData.desc,
                        link: apiData.link,
                        list: methodListTemp
                    };
                    searchArr.push(data);
                }
            }
        }
        let html;
        if (searchValue == '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchArr,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiData, liClass, display) {
    let html = "";
    let doc;
    if (apiData.length > 0) {
        for (let j = 0; j < apiData.length; j++) {
            html += '<li class="'+liClass+'">';
            html += '<a class="dd" href="#_' + apiData[j].link + '">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
            html += '<ul class="sectlevel2" style="'+display+'">';
            doc = apiData[j].list;
            for (let m = 0; m < doc.length; m++) {
                html += '<li><a href="#_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + doc[m].desc + '</a> </li>';
            }
            html += '</ul>';
            html += '</li>';
        }
    }
    return html;
}