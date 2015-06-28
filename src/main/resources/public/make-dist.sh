#!/bin/bash

# Simple script to generate self-contained bundles for each element.

# This should probably be a Plumber pipeline, but hey, this works.

mkdir -p dist tmp

# For each element
ls elements | grep '\.html$' | sed s/.html// | while read element
do
    html_bundle="tmp/$element.vulcanized.html"
    js_bundle="tmp/$element.bundle.js"
    target="dist/$element.html"

    node_modules/.bin/vulcanize --inline -o $html_bundle elements/$element.html || exit 1
    node_modules/.bin/jspm bundle windsock/elements/$element $js_bundle || exit 1

    # prepend JS bundle
    # Note: we could also link to it as a sync external resource to be
    # more CSP friendly?
    echo '<script>'  >  $target
    # FIXME: We want to setup the mapping for our libs (Reqwest); the
    # simplest way is to inject the whole of the config, which also
    # contains the paths mapping. That is unlikely to be what a
    # consumer wants, but the consumer will likely override those
    # anyway.
    cat config.js    >> $target
    # FIXME: Because Reqwest (an AMD module) is getting injected in as
    # part of the js_bundle, we need to ensure that SystemJS' define()
    # is being used instead of any other globally registered define()
    # (typically RequireJS if used in parallel).
    # Hopefully we can get rid of this hack once SystemJS 0.6.8 lands
    # with support for concurrent RequireJS/SystemJS?
    echo 'var oldDefine = window.define;'    >> $target
    echo 'window.define = System.amdDefine;' >> $target
    cat $js_bundle   >> $target
    echo 'window.define = oldDefine;'        >> $target
    echo '</script>' >> $target

    # then inject HTML bundle
    cat $html_bundle >> $target
done

rm -rf tmp
