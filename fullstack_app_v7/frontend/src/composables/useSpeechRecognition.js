import { ref } from 'vue'

export function useSpeechRecognition() {
  const isListening = ref(false)
  const transcript = ref('')
  const error = ref(null)
  let recognition = null

  const initializeSpeechRecognition = () => {
    try {
      if ('webkitSpeechRecognition' in window) {
        recognition = new webkitSpeechRecognition()
        recognition.continuous = false
        recognition.interimResults = false
        recognition.lang = 'en-US'

        recognition.onstart = () => {
          console.log('Speech recognition started')
          isListening.value = true
          error.value = null
        }

        recognition.onresult = (event) => {
          console.log('Speech recognition result:', event.results)
          transcript.value = event.results[0][0].transcript
          isListening.value = false
        }

        recognition.onerror = (event) => {
          console.error('Speech recognition error:', event.error)
          error.value = event.error
          isListening.value = false
        }

        recognition.onend = () => {
          console.log('Speech recognition ended')
          isListening.value = false
        }
      } else {
        error.value = 'Speech recognition is not supported in this browser'
        console.error('Speech recognition not supported')
      }
    } catch (err) {
      console.error('Error initializing speech recognition:', err)
      error.value = 'Failed to initialize speech recognition'
    }
  }

  const startListening = () => {
    try {
      if (!recognition) {
        initializeSpeechRecognition()
      }
      if (recognition) {
        transcript.value = ''
        recognition.start()
      } else {
        error.value = 'Speech recognition not available'
      }
    } catch (err) {
      console.error('Error starting speech recognition:', err)
      error.value = 'Failed to start speech recognition'
    }
  }

  const stopListening = () => {
    try {
      if (recognition) {
        recognition.stop()
      }
    } catch (err) {
      console.error('Error stopping speech recognition:', err)
      error.value = 'Failed to stop speech recognition'
    }
  }

  return {
    isListening,
    transcript,
    error,
    startListening,
    stopListening
  }
} 