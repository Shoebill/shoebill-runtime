# Shoebill Runtime

[![Build Status](http://ci.gtaun.net/app/rest/builds/buildType:(id:Shoebill_Runtime_Deploy)/statusIcon)](http://ci.gtaun.net/project.html?projectId=Shoebill_Runtime)

# What is the shoebill-runtime?

The shoebill-runtime is one of the main core parts of shoebill. Shoebill-runtime will implement all the interfaces provided
by shoebill-api. Without a runtime, shoebill cannot run correctly. Shoebill provide a official runtime, but you
can also create your own, if you don't like ours. Internally, the runtime will call the native SampFunctions everytime
you call some function like player.setName();

# How to use it?

You won't have to manually use a runtime for shoebill. It is already included in the resources.yml, but you may have
to change the version number if something changed.
