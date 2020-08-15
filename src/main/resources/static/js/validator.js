const validator = {
    isDigit: (value, maxDigit = 3, minDigit = 1) => {
        const regex = RegExp(`/^\\d+$/{${minDigit},${maxDigit}}`, 'g');
        return regex.test(value);
    }
};