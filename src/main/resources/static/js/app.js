document.addEventListener('DOMContentLoaded', () => {
    const urlInput = document.getElementById('urlInput');
    const createButton = document.getElementById('createButton');
    const responseDiv = document.getElementById('response');
    const responseContent = document.getElementById('responseContent');
    const copyButton = document.getElementById('copyButton');

    createButton.addEventListener('click', () => {
        responseDiv.style.visibility = 'visible';
        const urlValue = urlInput.value;

        if (urlValue.trim() === '') {
            responseContent.innerText = 'Please enter a URL.';
            return;
        }

        const dto = { longUrl: urlValue }

        fetch('/api/url/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dto),
        })
            .then(async response => {
                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(`HTTP Error ${response.status}: ${errorData.error}`);
                }
                return response.json();
            })
            .then(data => {
                responseContent.innerText = `${data.shortUrl}`;
            })
            .catch(error => {
                responseContent.innerText = `${error.message}`;
            });

    });

    function copyResponse() {
        const textToCopy = responseContent.innerText;
        const tempTextArea = document.createElement('textarea');
        tempTextArea.value = textToCopy;
        document.body.appendChild(tempTextArea);
        tempTextArea.select();
        document.execCommand('copy');
        document.body.removeChild(tempTextArea);
        copyButton.innerText = 'Copied!';

        setTimeout(() => {
            copyButton.innerText = 'Copy';
        }, 1000);
    }

    copyButton.addEventListener('click', copyResponse);
});
