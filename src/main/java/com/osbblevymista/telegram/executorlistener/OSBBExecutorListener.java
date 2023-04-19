package com.osbblevymista.telegram.executorlistener;

import com.osbblevymista.telegram.view.keyabords.KeyboardParam;

import java.io.IOException;
import java.net.URISyntaxException;

public interface OSBBExecutorListener {

    public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException;
}
