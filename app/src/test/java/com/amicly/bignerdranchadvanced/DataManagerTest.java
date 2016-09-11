package com.amicly.bignerdranchadvanced;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import retrofit2.Callback;

import static com.amicly.bignerdranchadvanced.DataManager.VenueSearchListener;
import static com.amicly.bignerdranchadvanced.DataManager.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by darrankelinske on 9/8/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 23, constants = BuildConfig.class)
public class DataManagerTest {
    @Captor
    private ArgumentCaptor<Callback<VenueSearchResponse>> argumentCaptor;
    private DataManager dataManager;
    @Mock
    private static VenueInterface venueInterface;
    private static VenueSearchListener venueSearchListener = mock(VenueSearchListener.class);


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dataManager = get(RuntimeEnvironment.application);

        dataManager.addVenueSearchListener(venueSearchListener);

    }

    @Test
    public void testRobolectricSetupWorks() {
        assertThat(1, equalTo(1));
    }



}
