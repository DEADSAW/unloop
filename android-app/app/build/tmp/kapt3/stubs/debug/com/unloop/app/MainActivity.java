package com.unloop.app;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0019H\u0002J\b\u0010\u001b\u001a\u00020\u0019H\u0002J\u0012\u0010\u001c\u001a\u00020\u00132\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J\b\u0010\u001f\u001a\u00020\u0013H\u0014J\b\u0010 \u001a\u00020\u0013H\u0014J\b\u0010!\u001a\u00020\u0013H\u0002J\b\u0010\"\u001a\u00020\u0013H\u0002J\b\u0010#\u001a\u00020\u0013H\u0002J\b\u0010$\u001a\u00020\u0013H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\n\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006%"}, d2 = {"Lcom/unloop/app/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/unloop/app/databinding/ActivityMainBinding;", "database", "Lcom/unloop/app/data/UnloopDatabase;", "getDatabase", "()Lcom/unloop/app/data/UnloopDatabase;", "database$delegate", "Lkotlin/Lazy;", "songSkippedReceiver", "Landroid/content/BroadcastReceiver;", "statsRepository", "Lcom/unloop/app/data/StatsRepository;", "getStatsRepository", "()Lcom/unloop/app/data/StatsRepository;", "statsRepository$delegate", "checkPermissions", "", "getTimeAgo", "", "timestamp", "", "isAccessibilityServiceEnabled", "", "isBatteryOptimizationDisabled", "isNotificationListenerEnabled", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onResume", "refreshStats", "registerReceivers", "setupUI", "showSkipHistoryDialog", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.unloop.app.databinding.ActivityMainBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy statsRepository$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver songSkippedReceiver = null;
    
    public MainActivity() {
        super();
    }
    
    private final com.unloop.app.data.UnloopDatabase getDatabase() {
        return null;
    }
    
    private final com.unloop.app.data.StatsRepository getStatsRepository() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void setupUI() {
    }
    
    private final void checkPermissions() {
    }
    
    private final boolean isBatteryOptimizationDisabled() {
        return false;
    }
    
    private final boolean isAccessibilityServiceEnabled() {
        return false;
    }
    
    private final boolean isNotificationListenerEnabled() {
        return false;
    }
    
    private final void registerReceivers() {
    }
    
    private final void refreshStats() {
    }
    
    private final void showSkipHistoryDialog() {
    }
    
    private final java.lang.String getTimeAgo(long timestamp) {
        return null;
    }
}