package org.wikipedia.translation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.TextUtils;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by aman_ on 3/30/2018.
 */
public class TranslatorTest{

    @Test
    public void testExecute() {
        Translator mockTranslator = PowerMockito.mock(Translator.class);
        Translator translator = new Translator();
        try{
            when(mockTranslator.execute("hello","en","fr")).thenReturn(translator);
            assertEquals(translator, mockTranslator.execute("hello","en","fr"));
        }catch(Exception e) {
            e.getStackTrace();
        }
    }
}