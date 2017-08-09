zk.afterLoad('zul.wnd', function () {
	var _panel = {};

zk.override(zul.wnd.Panel.prototype, _panel, {
	_sclass: 'panel-default',
	getZclass: function () {
		return 'panel';
	},
	$s: function (subclass) {
		switch (subclass) {
		case 'head':
			subclass = 'heading';
			break;
		case 'header':
			return '';
		}

		return _panel.$s.apply(this, arguments);
	}
});

var _panelchildren = {};

zk.override(zul.wnd.Panelchildren.prototype, _panelchildren, {
getZclass: function () {
	return '';
},
$s: function (subclass) {
	return '';
}
});

});