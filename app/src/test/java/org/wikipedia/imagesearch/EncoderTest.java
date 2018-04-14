package org.wikipedia.imagesearch;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by amawai on 2018-04-13.
 */

public class EncoderTest {
    private Encoder encoder;
    private Context mContext;
    private Uri mUri;
    private ContentResolver mContentResolver;
    private InputStream mInputStream;


    @Before
    public void setUp() throws IOException {
        encoder = new Encoder();

        mContext = mock(Context.class);
        mUri = mock(Uri.class);
        mContentResolver = mock(ContentResolver.class);
        mInputStream = mock(InputStream.class);

        when(mUri.getScheme()).thenReturn("not content");
        when(mUri.getPath()).thenReturn("/assets/mipmap/launcher.png/launcher.png");
        when(mContext.getContentResolver()).thenReturn(mContentResolver);
        when(mContentResolver.openInputStream(mUri)).thenReturn(mInputStream);
    }

    @Test
    public void encoderTest() throws IOException {
        encoder.encodeUriToBase64Binary(mContext, mUri);

        verify(mContext).getContentResolver();
        verify(mUri).getPath();
        verify(mUri).getPath();
        verify(mContentResolver).openInputStream(mUri);
        verify(mInputStream).read(any());
    }

    @Test
    public void testEncoderWithNullValues() throws FileNotFoundException {
        assertNull(encoder.encodeUriToBase64Binary(null, null));
    }
}
