// FIXME: can't use ../ possibly due to a SystemJS bug?
import {types} from 'windsock:scripts/model';

// FIXME: This is a hack to ensure the windsock-data definition is
// executed before the windsock-widget one. Otherwise, data bindings
// don't work properly. Very ugly to do this both in the HTML and JS
// files...
import './windsock-data';

Polymer('windsock-widget', {
  toggle: function() {
    var collapsed = this.classList.contains('collapsed');
    if (this.hasNotices() || ! collapsed) {
      this.classList.toggle('collapsed');
    }
  },

  noticesChanged: function(notices) {
    notices = notices || [];

    var classList = this.classList;

    classList.toggle('has-notices', notices.length > 0);

    // Update 'has-notices--{type}' class state
    types.forEach(function(type) {
      classList.remove('has-notices--' + type);
    });
    notices.forEach(function(notice) {
      classList.add('has-notices--' + notice.data.notice.type);
    });
  },

  hasNotices: function() {
    return this.notices && this.notices.length > 0;
  }
});
