// FIXME: This is a hack to ensure the windsock-data definition is
// executed before the windsock-widget one. Otherwise, data bindings
// don't work properly. Very ugly to do this both in the HTML and JS
// files...
import './windsock-data';


Polymer({
  is: 'windsock-status-view',

  properties: {
    notices: Array
  },

  /* == Filters == */

  pluralize: function(noun, count) {
    // TODO: smarter
    return noun + (count != 1 ? 's' : '');
  }
});


// Note: this needs to come after windsock-status-view

Polymer({
  is: 'windsock-status',

  properties: {
    src: String,
    refresh: Function
  }
});
