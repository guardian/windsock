import reqwest from 'github:ded/reqwest';

// Wrap in native Promise, since the reqwest promises don't flatMap
// correctly.
function reqwestBetter(...args) {
  return Promise.resolve(reqwest(...args));
}

export default class WindsockApi {
  constructor(apiUrl) {
    this.apiUrl = apiUrl;
  }

  listNotices() {
    var requestRoot = reqwestBetter(this.apiUrl);
    return requestRoot.
      then(function(resp) {
        return resp.links.filter(link => link.rel === 'notices')[0].href;
      }).
      then(href => reqwestBetter(href)).
      then(resp => resp.data);
  }

  addNotice(text, type, author) {
    var requestRoot = reqwestBetter(this.apiUrl);
    return requestRoot.
      then(function(resp) {
        return resp.links.filter(link => link.rel === 'notices')[0].href;
      }).
      then(href => {
        return reqwestBetter({
          url: href,
          method: 'post',
          type: 'json',
          contentType: 'application/json',
          data: JSON.stringify({
            text: text,
            type: type,
            author: author
          })
        });
      });
  }

  updateNotice(notice) {
    return reqwestBetter({
      url: notice.uri,
      method: 'put',
      type: 'json',
      contentType: 'application/json', // FIXME: post argo
      data: JSON.stringify(notice.data.notice)
    });
  }

  deleteNotice(notice) {
    return reqwestBetter({
      url: notice.uri,
      method: 'delete',
      type: 'json'
    });
  }
}
