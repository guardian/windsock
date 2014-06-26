import WindsockApi from '~/scripts/api';

Polymer('windsock-data', {
  ready: function() {
    this.api = new WindsockApi(this.src);

    this.loadNotices();
  },

  attachedCallback: function() {
    if (this.refresh) {
      var reload = this.loadNotices.bind(this);
      this.refreshInterval = setInterval(reload, this.refresh);
    }
  },

  detachedCallback: function() {
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
