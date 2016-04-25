package com.ewintory.yandex.mobilization.ui.activity;

@Deprecated
public final class SearchActivity extends BaseActivity {

//    @Bind(R.id.toolbar) Toolbar mToolbar;
//    @Bind(R.id.search_view) SearchView mSearchView;
//    @Bind(R.id.search_results) ListView mSearchResults;
//    private String mQuery = "";
//    private SearchAdapter mResultsAdapter;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedState) {
//        super.onCreate(savedState);
//        setContentView(R.layout.activity_search);
//        setSupportActionBar(mToolbar);
//    }
//
//    private void setupSearchView() {
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        mSearchView.setIconified(false);
//        // Set the query hint.
//        mSearchView.setQueryHint(getString(R.string.search_hint));
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                mSearchView.clearFocus();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                searchFor(s);
//                return true;
//            }
//        });
//        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                dismiss(null);
//                return false;
//            }
//        });
//        if (!TextUtils.isEmpty(mQuery)) {
//            mSearchView.setQuery(mQuery, false);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        dismiss(null);
//    }
//
//    public void dismiss(View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            doExitAnim();
//        } else {
//            ActivityCompat.finishAfterTransition(this);
//        }
//    }
//
//    /**
//     * On Lollipop+ perform a circular reveal animation (an expanding circular mask) when showing
//     * the search panel.
//     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void doEnterAnim() {
//        // Fade in a background scrim as this is a floating window. We could have used a
//        // translucent window background but this approach allows us to turn off window animation &
//        // overlap the fade with the reveal animation – making it feel snappier.
//        View scrim = findViewById(R.id.scrim);
//        scrim.animate()
//                .alpha(1f)
//                .setDuration(500L)
//                .setInterpolator(
//                        AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
//                .start();
//
//        // Next perform the circular reveal on the search panel
//        final View searchPanel = findViewById(R.id.search_panel);
//        if (searchPanel != null) {
//            // We use a view tree observer to set this up once the view is measured & laid out
//            searchPanel.getViewTreeObserver().addOnPreDrawListener(
//                    new ViewTreeObserver.OnPreDrawListener() {
//                        @Override
//                        public boolean onPreDraw() {
//                            searchPanel.getViewTreeObserver().removeOnPreDrawListener(this);
//                            // As the height will change once the initial suggestions are delivered by the
//                            // loader, we can't use the search panels height to calculate the final radius
//                            // so we fall back to it's parent to be safe
//                            int revealRadius = ((ViewGroup) searchPanel.getParent()).getHeight();
//                            // Center the animation on the top right of the panel i.e. near to the
//                            // search button which launched this screen.
//                            Animator show = ViewAnimationUtils.createCircularReveal(searchPanel,
//                                    searchPanel.getRight(), searchPanel.getTop(), 0f, revealRadius);
//                            show.setDuration(250L);
//                            show.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this,
//                                    android.R.interpolator.fast_out_slow_in));
//                            show.start();
//                            return false;
//                        }
//                    });
//        }
//    }
//
//    /**
//     * On Lollipop+ perform a circular animation (a contracting circular mask) when hiding the
//     * search panel.
//     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void doExitAnim() {
//        final View searchPanel = findViewById(R.id.search_panel);
//        // Center the animation on the top right of the panel i.e. near to the search button which
//        // launched this screen. The starting radius therefore is the diagonal distance from the top
//        // right to the bottom left
//        int revealRadius = (int) Math.sqrt(Math.pow(searchPanel.getWidth(), 2)
//                + Math.pow(searchPanel.getHeight(), 2));
//        // Animating the radius to 0 produces the contracting effect
//        Animator shrink = ViewAnimationUtils.createCircularReveal(searchPanel,
//                searchPanel.getRight(), searchPanel.getTop(), revealRadius, 0f);
//        shrink.setDuration(200L);
//        shrink.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this,
//                android.R.interpolator.fast_out_slow_in));
//        shrink.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                searchPanel.setVisibility(View.INVISIBLE);
//                ActivityCompat.finishAfterTransition(SearchActivity.this);
//            }
//        });
//        shrink.start();
//
//        // We also animate out the translucent background at the same time.
//        findViewById(R.id.scrim).animate()
//                .alpha(0f)
//                .setDuration(200L)
//                .setInterpolator(
//                        AnimationUtils.loadInterpolator(SearchActivity.this,
//                                android.R.interpolator.fast_out_slow_in))
//                .start();
//    }
//
//    private void searchFor(String query) {
//        // ANALYTICS EVENT: Start a search on the Search activity
//        // Contains: Nothing (Event params are constant:  Search query not included)
//        AnalyticsHelper.sendEvent(SCREEN_LABEL, "Search", "");
//        Bundle args = new Bundle(1);
//        if (query == null) {
//            query = "";
//        }
//        args.putString(ARG_QUERY, query);
//        if (TextUtils.equals(query, mQuery)) {
//            getLoaderManager().initLoader(SearchTopicsSessionsQuery.TOKEN, args, this);
//        } else {
//            getLoaderManager().restartLoader(SearchTopicsSessionsQuery.TOKEN, args, this);
//        }
//        mQuery = query;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (isFinishing()) {
//            overridePendingTransition(0, 0);
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_search) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private class SearchAdapter extends BaseAdapter {
//        @Override public int getCount() {
//            return 0;
//        }
//
//        @Override public Object getItem(int position) {
//            return null;
//        }
//
//        @Override public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override public View getView(int position, View convertView, ViewGroup parent) {
//            return null;
//        }
//    }
}
