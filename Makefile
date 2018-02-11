check:
	./gradlew clean check bintrayUpload

publish: check
	./gradlew releng
	./gradlew -PdryRun=false --info plugin:bintrayUpload

update-examples:
	./gradlew easylauncher
	cp ./example-simple/build/generated/easylauncher/res/debug/mipmap-xxhdpi/ic_launcher.png ic-debug.png
	cp ./example-custom/build/generated/easylauncher/res/localBeta/mipmap-xxhdpi/ic_launcher.png ic-beta.png
