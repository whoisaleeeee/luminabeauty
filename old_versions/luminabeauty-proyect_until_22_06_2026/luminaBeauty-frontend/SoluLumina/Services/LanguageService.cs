using System;

namespace LuminaBeauty.Services
{
    public class LanguageService
    {
        private string _currentLanguage = "es";

        public event Action? OnChange;

        public string CurrentLanguage => _currentLanguage;

        public void SetLanguage(string lang)
        {
            if (lang == "es" || lang == "en")
            {
                _currentLanguage = lang;
                OnChange?.Invoke();
            }
        }

        public string Translate(string key)
        {
            if (Data.BeautyDb.Translations.TryGetValue(_currentLanguage, out var transTable))
            {
                if (transTable.TryGetValue(key, out var translated))
                {
                    return translated;
                }
            }
            return key; // Fallback to key itself
        }
    }
}
