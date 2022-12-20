import { useEffect } from 'react';

export default function Fullscreen() {
  document.fullscreenEnabled =
    document.fullscreenEnabled ||
    document.mozFullScreenEnabled ||
    document.documentElement.webkitRequestFullScreen;

  function requestFullscreen(element) {
    console.log('히히');
    if (element.requestFullscreen) {
      element.requestFullscreen();
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen();
    } else if (element.webkitRequestFullScreen) {
      element.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
    }
  }

  useEffect(() => {
    console.log('하이', document.fullscreenEnabled);
    if (document.fullscreenEnabled) {
      console.log('hello');
      console.log(document.documentElement);
      requestFullscreen(document.documentElement);
    }
  }, []);
  return (
    <button
      onClick={() => {
        requestFullscreen(document.documentElement);
      }}
    >
      눌러봐!
    </button>
  );
}
