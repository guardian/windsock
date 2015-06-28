// FIXME: can't use ../ possibly due to a SystemJS bug?
import WindsockApi from 'windsock/scripts/api';

Polymer('windsock-data', {
  observe: {
    src: 'restart',
    refresh: 'restart'
  },

  ready: function() {
    this.restart();
  },

  attached: function() {
    this.restart();
  },

  detached: function() {
    this.stop();
  },

  restart: function() {
    this.stop();

    if (this.src) {
      this.api = new WindsockApi(this.src);

      if (this.refresh) {
        var reload = this.loadNotices.bind(this);
        this.refreshInterval = setInterval(reload, this.refresh);
      }

      this.loadNotices();
    }
  },

  stop: function() {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  },

  loadNotices: function() {
    return this.api.listNotices().then(notices => {
      this['sink-notices'] = notices;
    });
  },

  addNotice: function(properties) {
    return this.api.addNotice(properties.text, properties.type, properties.author).then((newRecord) => {
      this['sink-notices'].unshift(newRecord);
    });
  },

  updateNotice: function(notice) {
    // No need to update the notices as the object was mutated in
    // place
    return this.api.updateNotice(notice);
  },

  deleteNotice: function(notice) {
    return this.api.deleteNotice(notice).then(() => {
      this['sink-notices'] = this['sink-notices'].filter((n) => n !== notice);
    });
  }
});
