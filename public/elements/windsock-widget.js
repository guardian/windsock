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

  hasNotices: function() {
    return this.notices && this.notices.length > 0;
  }
});

