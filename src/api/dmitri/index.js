module.exports = (req, res) => {
    const { name = 'friend' } = req.query;
    const body = `Hello, ${name}`;
    res.setHeader('Content-Type', 'text/plain');
    res.end(body);
}
