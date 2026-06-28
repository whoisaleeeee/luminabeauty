using System;
using System.Threading.Tasks;

namespace LuminaBeauty.Services
{
    public class ToastService
    {
        public string? CurrentText { get; private set; }
        public event Action? OnChange;

        public void ShowToast(string message)
        {
            CurrentText = message;
            NotifyStateChanged();

            // Auto dismiss after 3 seconds asynchronously
            _ = ClearToastAfterDelay();
        }

        private async Task ClearToastAfterDelay()
        {
            await Task.Delay(3000);
            CurrentText = null;
            NotifyStateChanged();
        }

        private void NotifyStateChanged() => OnChange?.Invoke();
    }
}
