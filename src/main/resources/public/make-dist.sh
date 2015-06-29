#!/bin/bash


set -e

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

    rm -f $target
    touch $target

    # prepend JS bundle
    # Note: we could also link to it as a sync external resource to be
    # more CSP friendly?
    echo '<script>'  >>  $target
    # FIXME: We want to setup the mapping for our libs (Reqwest); the
    # simplest way is to inject the whole of the config, which also
    # contains the paths mapping. That is unlikely to be what a
    # consumer wants, but the consumer will likely override those
    # anyway.
    cat config.js    >> $target
    cat $js_bundle   >> $target
    echo '</script>' >> $target

    # then inject HTML bundle
    cat $html_bundle >> $target

done

rm -rf tmp
