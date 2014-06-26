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
    this.api.listNotices().then(notices => {
      this['sink-notices'] = notices;
    });
  }
});
