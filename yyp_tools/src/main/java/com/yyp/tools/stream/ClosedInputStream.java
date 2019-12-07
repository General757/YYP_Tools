//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.stream;

import java.io.InputStream;

/**
 * Created by Yan on 2019/10/11.
 */
public class ClosedInputStream extends InputStream {
    public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

    public ClosedInputStream() {
    }

    public int read() {
        return -1;
    }
}

