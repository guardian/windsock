import timeSince from 'windsock:scripts/time-since';

// FIXME: This is a hack to ensure the windsock-data definition is
// executed before the windsock-widget one. Otherwise, data bindings
// don't work properly. Very ugly to do this both in the HTML and JS
// files...
import './windsock-data';


Polymer('windsock-notices-view', {
  /* == Filters == */

  timeAgo: function(dateString) {
    var date = new Date(dateString);
    return timeSince(date) + ' ago';
  }
});


// Note: must come after windsock-notices-view

Polymer('windsock-notices');
