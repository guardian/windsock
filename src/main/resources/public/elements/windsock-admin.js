// FIXME: This is a hack to ensure the windsock-data definition is
// executed before the windsock-widget one. Otherwise, data bindings
// don't work properly. Very ugly to do this both in the HTML and JS
// files...
import './windsock-data';

Polymer({
  is: 'windsock-admin-add',
  ready: function() {
    this.resetForm();
  },

  addNotice: function() {
    this.fire('add-notice', this.form);
    return false; // cancel event
  },

  resetForm: function() {
    this.form = {};
  }
});


Polymer({
  is: 'windsock-admin-edit',
  updateNotice: function() {
    this.fire('update-notice', this.notice);
  },

  deleteNotice: function() {
    this.fire('delete-notice', this.notice);
  }
});


Polymer({
  is: 'windsock-admin',
  ready: function() {
    var data = this.$.data;
    var add = this.$.add;

    this.addEventListener('add-notice', (event) => {
      var properties = event.detail;
      data.addNotice(properties).then(add.resetForm.bind(add));
    });

    this.addEventListener('update-notice', (event) => {
      var noticeToUpdate = event.detail;
      data.updateNotice(noticeToUpdate);
    });

    this.addEventListener('delete-notice', (event) => {
      var noticeToDelete = event.detail;
      data.deleteNotice(noticeToDelete);
    });
  }
});
