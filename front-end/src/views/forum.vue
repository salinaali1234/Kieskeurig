<script>
export default {
  data() {
    return {
      postTitle: '', // this will hold the input text
      postContent: ''
    };
  },
  methods: {
    async submitPost() {
      try {
        const response = await fetch('http://localhost:8080/api/posts', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ title: this.postTitle, content: this.postContent, author : 1 })
        });

        const result = await response.json();
        console.log('Server response:', result);

        // Clear input after post
        this.postContent = '';
      } catch (error) {
        console.error('Failed to send post:', error);
      }
    }
  }
}
</script>

<template>
  <div>
    <h2>Forum</h2>
    <br />
    <p>
      Welkom op de forum pagina van Kieskeurig. <br />
      Heb je een vraag stel die vooral, of beantwoord vragen :D
    </p>
  </div>

  <div>
    <h3>Gesprekken</h3>
  </div>

  <div>
    <input v-model="postTitle" placeholder="Type je vraag hier..." />
    <input v-model="postContent" placeholder="Voor extra context">
    <button @click="submitPost">Post</button>
  </div>
</template>

<style>
div {
  margin-top: 20px;
}
</style>
