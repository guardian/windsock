import WindsockApi from '~/scripts/api';

// FIXME: This is a hack to ensure the windsock-data definition is
// executed before the windsock-widget one. Otherwise, data bindings
// don't work properly. Very ugly to do this both in the HTML and JS
// files...
import WindsockData from './windsock-data';

Polymer('windsock-admin-add', {
  ready: function() {
    this.resetForm();
  },

  addNotice: function() {
    // FIXME: share? or use windsock-data? don't recreate every time
    var api = new WindsockApi(this.src);
    api.addNotice(this.form.text, this.form.type, this.form.author).then((newRecord) => {
      this.resetForm();
      this.fire('notice-added', newRecord);
    });
    return false; // cancel event
  },

  resetForm: function() {
    this.form = {};
  }
});

Polymer('windsock-admin-edit', {
  ready: function() {
    // FIXME: share? don't recreate every time
    this.api = new WindsockApi(this.src);
  },

  updateNotice: function() {
    this.api.updateNotice(this.notice).then(() => {
      this.fire('notice-updated', this.notice);
    });
  },

  deleteNotice: function() {
    this.api.deleteNotice(this.notice).then(() => {
      this.fire('notice-deleted', this.notice);
    });
  }
});


Polymer('windsock-admin', {
  ready: function() {
    this.addEventListener('notice-added', (event) => {
      var addedNotice = event.detail;
      this.notices.unshift(addedNotice);
    });

    this.addEventListener('notice-deleted', (event) => {
      var deletedNotice = event.detail;
      this.notices = this.notices.filter((n) => n !== deletedNotice);
    });

    // Don't need to catch notice-updated since it's already updated in place
  }
});
