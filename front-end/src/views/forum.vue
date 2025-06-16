<script>
const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const getAllPostsUrl = `${backendUrl}/api/posts`;

export default {
  data() {
    return {
      postTitle: '',
      postContent: '',
      posts: [],
      commentsByPost: {},
      newComment: {},
      commentErrors: {},
      errors: {
        title: false,
        content: false
      }
    };
  },

  methods: {
    async fetchAllPosts() {
      try {
        const response = await fetch(getAllPostsUrl);
        if (response.ok) {
          const rawPosts = await response.json();

          // Sorteer eerst
          const sortedPosts = rawPosts.sort((a, b) => b.id - a.id);

          // Voeg reacties toe
          for (const post of sortedPosts) {
            const comments = await this.fetchComments(post.id);
            // post.comments = comments || [];
          }

          this.posts = sortedPosts;
        }
      } catch (error) {
        console.error("Failed to fetch posts:", error);
      }
    },

    async fetchComments(postId) {
      try {
        const response = await fetch(`${backendUrl}/api/comments/${postId}`);
        if (response.ok) {
          const comments = await response.json();
          this.commentsByPost[postId] = comments;
          return comments;
        }
      } catch (err) {
        console.error("Failed to fetch comments:", err);
      }
    },

    async submitPost() {
      this.errors.title = false;
      this.errors.content = false;

      try {
        const response = await fetch(`${backendUrl}/api/posts/create`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            title: this.postTitle,
            content: this.postContent,
            author: 1
          })
        });

        if (response.ok) {
          this.postTitle = '';
          this.postContent = '';
          await this.fetchAllPosts();
        } else {
          const errorResponse = await response.json();

          if (errorResponse.message?.includes("title")) {
            this.errors.title = true;
          }
          if (errorResponse.message?.includes("content")) {
            this.errors.content = true;
          }

          console.error('Validation error:', errorResponse);
        }
      } catch (error) {
        console.error('Failed to send post:', error);
      }
    },

    async submitComment(postId) {
      const content = this.newComment[postId];
      if (!content) {
        this.commentErrors[postId] = true;
        return;
      } else {
        this.commentErrors[postId] = false;
      }

      try {
        const response = await fetch(`${backendUrl}/api/comments`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            postId,
            content,
            author: 1
          })
        });

        if (response.ok) {
          this.newComment[postId] = "";
          await this.fetchComments(postId);
        }
      } catch (err) {
        console.error("Failed to post comment:", err);
      }
    }
  },

  mounted() {
    this.fetchAllPosts();
  }
};
</script>

<template>
  <div>
    <h2>Forum</h2>
    <p>
      Welkom op de forum pagina van Kieskeurig. <br />
      Heb je een vraag stel die vooral, of beantwoord vragen :D
    </p>

    <div class="new-post">
      <h3>Nieuwe Post</h3>
      <input
        v-model="postTitle"
        :class="{ 'input-error': errors.title }"
        placeholder="Type je vraag hier..."
      />
      <input
        v-model="postContent"
        :class="{ 'input-error': errors.content }"
        placeholder="Voor extra context"
      />
      <button @click="submitPost">Post</button>
    </div>

    <div>
      <h3>Gesprekken</h3>
      <ul>
        <li class="posts" v-for="post in posts" :key="post.id">
          <strong>{{ post.title }}</strong><br />
          {{ post.content }}

          <div class="comments" v-if="post.comments && post.comments.length > 0">
            <h4>Reacties:</h4>
            <ul>
              <li v-for="comment in post.comments" :key="comment.id">
                {{ comment.content }}
              </li>
            </ul>
          </div>

          <input
            v-model="newComment[post.id]"
            :class="{ 'input-error': commentErrors[post.id] }"
            placeholder="Typ een reactie..."
          />

          <button @click="submitComment(post.id)">Reageer</button>
        </li>

      </ul>
    </div>


  </div>
</template>

<style scoped>
:root {
  --primary-clr: #3b214b;
  --secondary-clr: #DEBFE9;
  --accent-clr: #ffcc00;
  --text-clr: #ffffff;
  --light-purple: #a084c6;
}

body {
  background-color: var(--primary-clr);
  margin: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

h2 {
  color: var(--text-clr);
  max-width: 700px;
  margin: 0 auto 25px auto;
  padding: 0 30px;
  text-align: center;
  font-size: 2.8rem;
  font-weight: 700;
}

p {
  color: var(--text-clr);
  max-width: 700px;
  margin: 0 auto 40px auto;
  padding: 0 30px;
  text-align: center;
  font-size: 1.3rem;
  font-weight: 400;
  line-height: 1.5;
}

.new-post, .posts {
  background-color: white;
  color: black;
  max-width: 700px;
  margin: 20px auto;
  padding: 25px 30px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(59,33,75,0.15);
  font-size: 1.15rem;
}

.posts strong {
  color: var(--primary-clr);
  font-size: 1.35rem;
  display: block;
  margin-bottom: 12px;
  font-weight: 700;
}

input {
  display: block;
  margin: 15px 0;
  padding: 14px 12px;
  width: 100%;
  max-width: 600px;
  border-radius: 8px;
  border: 1.5px solid var(--light-purple);
  font-size: 1.1rem;
  background-color: #f6f2ff;
  color: var(--primary-clr);
  transition: border-color 0.3s ease;
}

input::placeholder {
  color: var(--light-purple);
}

input:focus {
  border-color: var(--accent-clr);
  outline: none;
  background-color: #eae3ff;
  color: var(--primary-clr);
}

.input-error {
  border: 2px solid var(--accent-clr);
  outline: none;
  background-color: #fff8c4;
  color: #333;
}

button {
  padding: 12px 24px;
  background: var(--accent-clr);
  color: var(--primary-clr);
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  cursor: pointer;
  font-weight: 700;
  transition: background-color 0.3s ease;
  margin-top: 10px;
}

button:hover {
  background: #e6b800;
}

.comments {
  margin-top: 15px;
  padding-left: 15px;
  border-left: 4px solid var(--light-purple);
  font-size: 1.05rem;
  color: var(--primary-clr);
}

.comments ul li {
  margin-bottom: 8px;
}

ul {
  list-style-type: none;
  padding-left: 0;
}


@media (max-width: 900px) {
  .new-post, .posts {
    max-width: 90%;
    padding: 20px 20px;
    font-size: 1.05rem;
  }

  input {
    max-width: 100%;
    font-size: 1rem;
  }

  button {
    font-size: 1rem;
    padding: 10px 20px;
  }

  h2 {
    font-size: 2.4rem;
    padding: 0 20px;
  }

  p {
    font-size: 1.15rem;
    padding: 0 20px;
  }
}

@media (max-width: 480px) {
  .new-post, .posts {
    padding: 15px 15px;
    font-size: 1rem;
  }

  input {
    font-size: 0.95rem;
  }

  button {
    font-size: 0.95rem;
    padding: 8px 16px;
  }

  h2 {
    font-size: 1.9rem;
    padding: 0 15px;
  }

  p {
    font-size: 1rem;
    padding: 0 15px;
  }
}

</style>
