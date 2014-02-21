gconftool-2 -t string -s /desktop/gnome/url-handlers/monacoin/command "java -splash:doesnotexist.png -jar $INSTALL_PATH/multimona-exe.jar %s"
gconftool-2 -s /desktop/gnome/url-handlers/monacoin/needs_terminal false -t bool
gconftool-2 -t bool -s /desktop/gnome/url-handlers/monacoin/enabled true