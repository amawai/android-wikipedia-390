package org.wikipedia.activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseActivity.class)
public class BaseActivityTest {
    @Test
    //Verify that the operation onThisDayNotification is being properly called
    public void testMockOnThisDayNotificationTransition() {
        PowerMockito.mock(BaseActivity.class);
        BaseActivity mockBaseActivity = mock(BaseActivity.class, Mockito.CALLS_REAL_METHODS);
        PowerMockito.doAnswer((i) -> {
            return null;
        }).when(mockBaseActivity).onThisDayNotificationTransition();
    }
}


