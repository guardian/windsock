// FIXME: can't use ../ possibly due to a SystemJS bug?
import timeSince from 'windsock/scripts/time-since';

// FIXME: This is a hack to ensure the windsock-data definition is
// executed before the windsock-widget one. Otherwise, data bindings
// don't work properly. Very ugly to do this both in the HTML and JS
// files...
import './windsock-data';


Polymer({
  is: 'windsock-notices-view',

  properties: {
    notices: Array
  },

  /* == Filters == */

  isEmpty: function(array) {
    return array.length === 0;
  },

  isNotEmpty: function(array) {
    return array.length !== 0;
  },

  classNoticeType: function(type) {
    return `notice__type notice__type--${type}`;
  },

  timeAgo: function(dateString) {
    var date = new Date(dateString);
    return timeSince(date) + ' ago';
  }
});


// Note: must come after windsock-notices-view

Polymer({
  is: 'windsock-notices',

  properties: {
    src: String,
    refresh: Function
  }
});
