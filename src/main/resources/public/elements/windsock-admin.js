// FIXME: This is a hack to ensure the windsock-data definition is
// executed before the windsock-widget one. Otherwise, data bindings
// don't work properly. Very ugly to do this both in the HTML and JS
// files...
import './windsock-data';

Polymer({
  is: 'windsock-admin-form',

  properties: {
    notice: {
      type: Object,
      notify: true
    }
  }
});

Polymer({
  is: 'windsock-admin-add',

  properties: {
    src: String
  },

  ready: function() {
    this.resetNewNotice();
  },

  addNotice: function(event) {
    // Don't let form submit and navigate
    event.preventDefault();

    this.fire('add-notice', this.newNotice);
  },

  resetNewNotice: function() {
    this.newNotice = {
      text:   '',
      type:   'minor', // TODO: avoid hardcoding default?
      author: ''
    };
  }
});


Polymer({
  is: 'windsock-admin-edit',

  properties: {
    notice: Object
  },

  updateNotice: function(event) {
    // Don't let form submit and navigate
    event.preventDefault();

    this.fire('update-notice', this.notice);
  },

  deleteNotice: function() {
    this.fire('delete-notice', this.notice);
  }
});


Polymer({
  is: 'windsock-admin',

  properties: {
    src: String
  },

  addNotice: function(event, notice) {
    var data = this.$.data;
    var add = this.$.add;

    data.addNotice(notice).then(add.resetNewNotice.bind(add));
  },

  updateNotice: function(event, notice) {
    var data = this.$.data;

    data.updateNotice(notice);
  },

  deleteNotice: function(event, notice) {
    var data = this.$.data;

    data.deleteNotice(notice);
  }
});
