package com.sa.ricky.clienteimportadora;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ricky on 27/10/2017.
 */
public class HttpRequestTaskTest {
    @Test
    public void onProgressUpdate() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.sa.ricky.clienteimportadora", appContext.getPackageName());
    }

}